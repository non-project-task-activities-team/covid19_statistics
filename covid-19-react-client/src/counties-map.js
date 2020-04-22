import React, {Component} from "react";
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import GeoJSON from 'ol/format/GeoJSON';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import {Fill, Stroke, Style, Text} from 'ol/style';
import {fromLonLat, toLonLat, transform} from 'ol/proj';
import ReactDOM from 'react-dom';
import Overlay from 'ol/Overlay';
import {getCenter} from 'ol/extent';
import {ApiRSocketClient} from './api-rsocket-client';
import {Metadata} from './metadata'

class CountiesMap extends Component {

  constructor(props) {
    super(props);
    this.setMapRef = element => {
      this.mapRef = element;
    };
    this.centerCoordinates = [31.46120621875, 48.5480583823923];
    this.showPopupInFeatureCenter = false;
    this.popupIsShown = false;

    this.rSocketClient =
        new ApiRSocketClient(
            'ws://localhost:8080/rsocket',
            new MapHandler(this.map)
        );

    // this.rSocketClient.connect()
    //   .then(() =>
    //       this.rSocketClient.fetchTotalCovid19Statistics(100)
    //         .subscribe({
    //             onSubscribe: sub => {
    //               this.useSubscription(sub)
    //             },
    //             onNext: msg => {
    //               console.log("onNext: ");
    //               console.log(msg.data);
    //             },
    //             onError: error => {
    //               console.error("onError: " + error);
    //               console.dir(error);
    //             }
    //         })
    //   )
    //   .then(v => console.log(v));
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

    self.countiesMap = new Map({
      layers: [self.getCountriesVectorLayer()],
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
      popup.content.innerHTML =
          '<h3 class="ol-popup-title"><strong>' + feature.get("name")
          + '</strong></h3>' +
          '<p><code>' + toLonLat(coordinate) + '</code></p>';
      popup.overlay.setPosition(coordinate);
      self.popupIsShown = true;
    });

    this.rSocketClient.connect().then(() =>
        this.rSocketClient.streamTotalCovid19Statistics(500, function(msg) {
          console.log(msg.data);
        })
    );
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

  render() {
    return (
        <div id="mapContainer">
          <div id="map" ref={this.setMapRef}></div>
          <div id="popup" className="ol-popup">
            <a href="#" id="popup-closer" className="ol-popup-closer"></a>
            <div id="popup-content"></div>
          </div>
        </div>
    )
  }

  getCurrentMapCenter() {
    let center = this.countiesMap.getView().getCenter();
    return transform(center, 'EPSG:3857', 'EPSG:4326');
  }

  getCountriesOverlayVectorLayer() {
    let style = new Style({
      stroke: new Stroke({
        color: '#f00',
        width: 1
      }),
      fill: new Fill({
        color: 'rgba(255,0,0,0.1)'
      }),
      text: new Text({
        font: '12px Calibri,sans-serif',
        fill: new Fill({
          color: '#000'
        }),
        stroke: new Stroke({
          color: '#f00',
          width: 3
        })
      })
    });

    return new VectorLayer({
      source: new VectorSource(),
      map: this.countiesMap,
      style: function (feature) {
        style.getText().setText(feature.get('name'));
        return style;
      }
    });
  }

  getCountriesVectorLayer() {
    let style = new Style({
      fill: new Fill({
        color: 'rgba(255, 100, 100, 0.3)'
      }),
      stroke: new Stroke({
        color: 'rgba(255, 80, 80, 0.9)',
        width: 2
      }),
      text: new Text({
        font: '12px Calibri,sans-serif',
        fill: new Fill({
          color: '#000'
        }),
        stroke: new Stroke({
          color: '#fff',
          width: 3
        })
      })
    });

    return new VectorLayer({
      source: new VectorSource({
        url: 'counties.geo.json',
        format: new GeoJSON()
      }),
      style: function (feature) {
        style.getText().setText(feature.get('name'));
        return style;
      }
    });
  }
}

class MapHandler {

  constructor(map) {
    this.map = map;
  }

  fireAndForget(payload) {
    if(payload.metadata.get(Metadata.ROUTE) == "send.to.location") {
      const radar = payload.data;
      this.map.panTo([radar.location.lat, radar.location.lng]);
    }
  }

}

export default CountiesMap;
