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

  fetchTotalCovid19Statistics(max, onNext) {
    // let metadata = new Metadata();
    // metadata.set(Metadata.ROUTE, 'covid19.statistics.total');
    // return this.socket.requestStream({
    //   metadata: metadata
    // });

      let statistics = [];
      return new Promise(((resolve, reject) => {
        let metadata = new Metadata();
        metadata.set(Metadata.ROUTE, 'covid19.statistics.total');
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
          // onNext: onNext,
          onComplete: () => resolve(statistics)
        });
      }));
  }

  disconnect() {
    this.cancel();
  }
}
