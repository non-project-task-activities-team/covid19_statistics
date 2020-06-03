import {JsonSerializer, RSocketClient} from 'rsocket-core';
import RSocketWebSocketClient from 'rsocket-websocket-client';
import {JsonMetadataSerializer, Metadata} from './metadata'

export class ApiRSocketClient {
  constructor(url, responder) {
    this.client = new RSocketClient({
      serializers: {
        data: JsonSerializer,
        metadata: JsonMetadataSerializer,
      },
      setup: {
        // ms btw sending keepalive to server
        keepAlive: 10000,
        // ms timeout if no keepalive response
        lifetime: 20000,
        dataMimeType: 'application/json',
        metadataMimeType: JsonMetadataSerializer.MIME_TYPE,
      },
      transport: new RSocketWebSocketClient({url: url}),
      responder: responder
    });
  }

  connect(cb) {
    let self = this;
    return new Promise(((resolve, reject) => {
      this.client.connect().subscribe({
        onComplete: s => {
          self.socket = s;
          resolve(self.socket);
        },
        onError: error => reject(error),
        onSubscribe: cancel => {
          this.cancel = cancel
        }
      });
    }));
  }

  streamGeneralCovid19Statistic(maxBackpresure) {
    let metadata = new Metadata();
    metadata.set(Metadata.ROUTE, 'covid19.statistics.general.stream');
    return this.socket.requestStream({
      data: { max: maxBackpresure },
      metadata: metadata
    });
  }

  getMaxGeneralCovid19Statistic(onComplete) {
    return new Promise(((resolve, reject) => {
      let metadata = new Metadata();
      metadata.set(Metadata.ROUTE, 'covid19.statistics.general.max');
      return this.socket.requestResponse({
        metadata: metadata,
      }).subscribe({
        onError: error => reject(error),
        onComplete: data => {
          onComplete(data);
          return resolve(data);
        }
      });
    }));
  }

  streamCovid19StatisticsByDatesRange(maxBackpresure, startDate, endDate) {
    let metadata = new Metadata();
    metadata.set(Metadata.ROUTE, 'covid19.statistics.daily.by_dates_range.stream');
    return this.socket.requestStream({
      data: {
        max: maxBackpresure, 
        startDate: startDate, 
        endDate: endDate
      }, 
      metadata: metadata
    });
  }

  disconnect() {
    this.cancel();
  }
}
