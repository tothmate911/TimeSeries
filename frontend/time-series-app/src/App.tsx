import axios from 'axios';
import React from 'react';
import Show from './components/Show';
import Upload from './components/Upload';

const backendUrl = "http://localhost:8080/api";
axios.defaults.baseURL = backendUrl;

export const App: React.FC = () => {

  return (
    <div className="app-container">
      <Upload />
      <Show />
    </div>
  );
};
