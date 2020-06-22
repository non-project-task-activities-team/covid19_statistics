import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import CountiesMap from './counties-map';
import * as serviceWorker from './serviceWorker';
import {BrowserRouter as Router, Switch, Route} from "react-router-dom";

function App() {
  return (
      <div>
        <h1 className="title">COVID-19 statistics</h1>
        <CountiesMap/>
      </div>
  );
}

const Root = () =>
  <Router>
    <Switch>
      <Route exact path="/">
        <App />
      </Route>
    </Switch>
  </Router>

export default App;

ReactDOM.render(
  <React.StrictMode>
    <Root />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
