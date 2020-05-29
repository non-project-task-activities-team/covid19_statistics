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

  streamTotalCovid19Statistics(max, onNext) {
      let statistics = [];
      return new Promise(((resolve, reject) => {
        let metadata = new Metadata();
        metadata.set(Metadata.ROUTE, 'covid19.statistics.total.stream');
        return this.socket.requestStream({
          data: {max: max},
          metadata: metadata,
        }).subscribe({
          onSubscribe: sub => sub.request(max),
          onError: error => reject(error),
          onNext: msg => {
            onNext(msg);
            statistics.push(msg.data);
          },
          onComplete: () => resolve(statistics)
        });
      }));
  }

  getMaxTotalCovid19Statistics(onComplete) {
    return new Promise(((resolve, reject) => {
      let metadata = new Metadata();
      metadata.set(Metadata.ROUTE, 'covid19.statistics.total.max');
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


// input parameters: start date, end date
  getCovid19StatisticsByDatesRange(startDate, endDate, onNext) {
    return new Promise(((resolve, reject) => {
      let metadata = new Metadata();
      metadata.set(Metadata.ROUTE, 'covid19.statistics.by.dates.range');
      return this.socket.requestStream({
        data: {
          startDate: startDate,
          endDate: endDate
        },
        metadata: metadata
      }).subscribe({
        onSubscribe: sub => sub.request(1000),
        onError: error => reject(error),
        onNext: msg => {
          onNext(msg);
        }
      });
    }));
  }

  disconnect() {
    this.cancel();
  }
}
