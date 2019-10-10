import React, { Component } from 'react';
import './styles/App.css';
import InstructorApp from './components/navigation/InstructorApp.jsx';
import { BrowserRouter } from 'react-router-dom';

class App extends Component {
  render() {
    return (
      <BrowserRouter>
      {/*<div className="container">*/}
        <div className="app">
        <InstructorApp />
      </div>
      </BrowserRouter>
    );
  }
}

export default App;