import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import CountiesMap from './counties-map';
import * as serviceWorker from './serviceWorker';
import DatePicker from './date-picker';

function App() {
  return (
      <div>
        <h1 className="title">COVID-19 statistics</h1>
          <div className="center">
            <DatePicker/>
          </div>
        <CountiesMap/>
      </div>
  );
}

export default App;

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
