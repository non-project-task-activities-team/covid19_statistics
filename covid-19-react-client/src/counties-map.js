import React, {Component} from "react";
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import GeoJSON from 'ol/format/GeoJSON';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import {Fill, Stroke, Style, Text} from 'ol/style';
import {fromLonLat, transform} from 'ol/proj';
import ReactDOM from 'react-dom';
import Overlay from 'ol/Overlay';
import {getCenter} from 'ol/extent';
import {ApiRSocketClient} from './api-rsocket-client';
import {MapUtils} from './map-utils';
import {Metadata} from './metadata'
import DatePicker from './date-picker';

class CountiesMap extends Component {

  constructor(props) {
    super(props);
    this.setMapRef = element => {
      this.mapRef = element;
    };
    this.centerCoordinates = [31.46120621875, 48.5480583823923];
    this.showPopupInFeatureCenter = false;
    this.popupIsShown = false;
    this.mapIsReady = false;
    this.maxBackpresure = 100;

    this.rSocketClient =
      new ApiRSocketClient(
        'ws://localhost:8080/rsocket',
        new MapHandler(this.map)
      );

    this.backpressureCtrl = new BackpressureCtrl(true, this.maxBackpresure);
  }

  initPopup() {
    let self = this;
    let content = document.getElementById('popup-content');
    let closer = document.getElementById('popup-closer');
    let container = document.getElementById('popup');

    let overlay = new Overlay({
      element: container,
      autoPan: true,
      autoPanAnimation: {
        duration: 250
      }
    });

    closer.onclick = function () {
      overlay.setPosition(undefined);
      closer.blur();
      self.popupIsShown = false;
      return false;
    };

    return {
      overlay: overlay,
      content: content,
      container: container,
      closer: closer
    }
  }

  componentDidMount() {
    let self = this;
    let popup = this.initPopup();

    self.rSocketClient.connect().then(() =>
        self.rSocketClient.getMaxGeneralCovid19Statistic(function (data) {
          self.maxGeneralConfirmed = data.data.confirmed;
          console.log("Current max general confirmed: " + self.maxGeneralConfirmed);
        })
    );

    self.countriesVectorLayer = self.getCountriesVectorLayer();
    self.countiesMap = new Map({
      layers: [self.countriesVectorLayer],
      overlays: [popup.overlay],
      target: ReactDOM.findDOMNode(this.mapRef),
      view: new View({
        center: fromLonLat(self.centerCoordinates),
        zoom: 6,
        minZoom: 2.5,
        maxZoom: 20
      })
    });

    self.featureOverlay = self.getCountriesOverlayVectorLayer();
    self.countiesMap.on('pointermove', function (evt) {
      if (evt.dragging || self.popupIsShown) {
        return;
      }
      let pixel = self.countiesMap.getEventPixel(evt.originalEvent);
      let feature = self.countiesMap.forEachFeatureAtPixel(pixel, f => f);
      self.highlightFeature(feature);
    });

    self.countiesMap.on('singleclick', function (evt) {
      let coordinate;
      let pixel = self.countiesMap.getEventPixel(evt.originalEvent);
      let feature = self.countiesMap.forEachFeatureAtPixel(pixel, f => f);
      if (!feature) {
        return;
      }
      self.highlightFeature(feature);
      if (self.showPopupInFeatureCenter) {
        let featureGeomExtent = feature.getGeometry().getExtent();
        coordinate = getCenter(featureGeomExtent);
      } else {
        coordinate = evt.coordinate;
      }
      if(feature.server_data !== undefined) {
        popup.content.innerHTML =
            '<h3 class="ol-popup-title"><strong>' + feature.get("name") + '</strong></h3>' +
            '<table class="ol-popup-table">' +
              '<tr class="table-warning">' +
                '<td>Confirmed</td>' +
                '<td>' + feature.server_data.confirmed + '</td>' +
              '</tr>' +
              '<tr class="table-danger">' +
                '<td>Deaths</td>' +
                '<td>' + feature.server_data.deaths + '</td>' +
              '</tr>' +
              '<tr class="table-success">' +
                '<td>Recovered</td>' +
                '<td>' + feature.server_data.recovered + '</td>' +
              '</tr>' +
            '</table>';
      } else {
        popup.content.innerHTML =
            '<h3 class="ol-popup-title">' +
              '<strong>' + feature.get("name") + '</strong>' +
            '</h3>' +
            '<div class="alert alert-secondary" role="alert">' +
              'There is no information!' +
            '</div>';
      }

      popup.overlay.setPosition(coordinate);
      self.popupIsShown = true;
    });

    self.countriesVectorLayer.getSource().on("change", function(e) {
      if(self.countriesVectorLayer.getSource().getState() === "ready" && !self.mapIsReady) {
        let features = self.countriesVectorLayer.getSource().getFeatures();
        self.rSocketClient.streamGeneralCovid19Statistic(self.maxBackpresure)
          .subscribe({
            onSubscribe: sub => self.backpressureCtrl.useSubscription(sub),
            onNext: payload => self.onNextRSocketStreamHandler(features, payload)
          });
        self.mapIsReady = true;
      }
    });
  }

  highlightFeature(feature) {
    if (feature !== this.highlightedFeature) {
      if (this.highlightedFeature) {
        this.featureOverlay.getSource().removeFeature(this.highlightedFeature);
      }
      if (feature) {
        this.featureOverlay.getSource().addFeature(feature);
      }
      this.highlightedFeature = feature;
    }
  }

  dateRangeSubmitButtonHandler(state) {
    let self = this;
    self.backpressureCtrl.cancel();
    self.maxGeneralConfirmed = 0;
    let features = self.countriesVectorLayer.getSource().getFeatures();
    features.forEach(feature => {
      feature.server_data = undefined;
      feature.setStyle(self.getFeatureStyle(feature));
    });
    let startDate = state.startDate.format("YYYY-MM-DD");
    let endDate = state.endDate.format("YYYY-MM-DD");
    self.maxBackpresure = state.maxBackpresure;
    self.rSocketClient.streamCovid19StatisticsByDatesRange(self.maxBackpresure, startDate, endDate)
      .subscribe({
        onSubscribe: sub => self.backpressureCtrl.useSubscription(sub),
        onNext: payload => self.onNextRSocketStreamHandler(features, payload)
      });
  }

  onNextRSocketStreamHandler(features, payload) {
    let self = this;
    console.log(payload.data);
    self.backpressureCtrl.decreaseCurrentDemand();
    features.filter(feature => feature.get("iso_a2") === payload.data.countryCode)
      .forEach(feature => {
        feature.server_data = payload.data;
        if(self.maxGeneralConfirmed < payload.data.confirmed) {
          self.maxGeneralConfirmed = payload.data.confirmed;
          console.log("New max general confirmed: " + self.maxGeneralConfirmed);
          features.forEach(feature => {
            feature.setStyle(self.getFeatureStyle(feature));
          });
        }
        feature.setStyle(self.getFeatureStyle(feature));
      });
  }

  render() {
    return (
      <div>
        <DatePicker maxBackpresure={this.maxBackpresure} dateRangeSubmitButtonHandler={this.dateRangeSubmitButtonHandler.bind(this)} />
        <div id="mapContainer">
          <div id="map" ref={this.setMapRef}></div>
          <div id="popup" className="ol-popup">
            <a href="#" id="popup-closer" className="ol-popup-closer"></a>
            <div id="popup-content"></div>
          </div>
        </div>
      </div>
    )
  }

  getCurrentMapCenter() {
    let center = this.countiesMap.getView().getCenter();
    return transform(center, 'EPSG:3857', 'EPSG:4326');
  }

  getCountriesVectorLayer() {
    let self = this;
    let source = new VectorSource({
      url: 'counties-low-resolution.geo.json',
      format: new GeoJSON()
    });
    return new VectorLayer({
      source: source,
      style: function (feature) {
        return self.getFeatureStyle(feature);
      }
    });
  }

  getCountriesOverlayVectorLayer() {
    let self = this;
    return new VectorLayer({
      source: new VectorSource(),
      map: this.countiesMap,
      style: function (feature) {
        return self.getHighlightedFeatureStyle(feature);
      }
    });
  }

  getFeatureStyle(feature) {
    let data = feature.server_data;
    let confirmed = 0;
    let opacity = 0.4;
    let title = feature.get('name');

    if(data !== undefined && data !== null) {
      confirmed = data.confirmed;
      title += '\n' + data.confirmed;
    }

    return new Style({
      fill: new Fill({
        color: MapUtils.calculateColor(this.maxGeneralConfirmed, confirmed, opacity)
      }),
      stroke: new Stroke({
        color: 'rgba(0, 0, 0, 0.5)',
        width: 2
      }),
      text: new Text({
        text: title,
        font: '14px Calibri,sans-serif',
        fill: new Fill({
          color: '#000'
        }),
        stroke: new Stroke({
          color: '#fff',
          width: 2
        })
      })
    });
  }

  getHighlightedFeatureStyle(feature) {
    let data = feature.server_data;
    let confirmed = 0;
    let opacity = 0.5;
    let title = feature.get('name');

    if(data !== undefined) {
      confirmed = data.confirmed;
      title += '\n' + data.confirmed;
    }

    return new Style({
      stroke: new Stroke({
        color: 'rgba(0, 0, 0, 1)',
        width: 1
      }),
      fill: new Fill({
        color:
            MapUtils.calculateColor(
                this.maxGeneralConfirmed,
                confirmed,
                opacity
            )
      }),
      text: new Text({
        text: title,
        font: '14px Calibri,sans-serif',
        fill: new Fill({
          color: '#000'
        }),
        stroke: new Stroke({
          color: '#fff',
          width: 2
        })
      })
    });
  }
}

class BackpressureCtrl {
  constructor(useTotalBackpresure, maxBackpresure) {
    this.useTotalBackpresure = useTotalBackpresure;
    this.maxBackpresure = maxBackpresure;
    if(!this.useTotalBackpresure) {
      window.setInterval(() => {
        let currentDemand = this.currentDemand;
        if (this.subscription && currentDemand < maxBackpresure) {
          this.subscription.request(maxBackpresure - currentDemand);
          this.currentDemand += maxBackpresure - currentDemand;
        }
      }, 1000);
    }
  }

  useSubscription(sub) {
    this.currentDemand = 0;
    this.subscription = sub;
    if(this.useTotalBackpresure) {
      this.subscription.request(this.maxBackpresure);
    }
  }

  decreaseCurrentDemand() {
    if(!this.useTotalBackpresure) {
      this.currentDemand--;
    }
  }
    
  cancel() {
    if (this.subscription) {
      this.subscription.cancel();
      this.subscription = null;
    }
  }
}

class MapHandler {
  constructor(map) {
    this.map = map;
  }

  fireAndForget(payload) {
    if(payload.metadata.get(Metadata.ROUTE) === "send.to.location") {
      const radar = payload.data;
      this.map.panTo([radar.location.lat, radar.location.lng]);
    }
  }
}

export default CountiesMap;
