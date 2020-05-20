import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import CountiesMap from './counties-map';
import * as serviceWorker from './serviceWorker';

function App() {
  return (
      <div>
        <h1 className="title">COVID-19 statistics</h1>
          <div className="form-inline center">
            <div className="form-group mx-sm-3 mb-2">
              <label htmlFor="startDate" className="my-1 mr-2">Start Date</label>
              <input type="date" className="form-control" id="startDate" placeholder="startDate"/>
            </div>
            <div className="form-group mx-sm-3 mb-2">
              <label htmlFor="endDate" className="my-1 mr-2">End Date</label>
              <input type="date" className="form-control" id="endDate" placeholder="endDate"/>
            </div>
            <button type="button" className="btn btn-primary mb-2">Submit</button>
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
