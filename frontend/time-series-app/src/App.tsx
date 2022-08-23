import React from 'react';
import Show from './components/Show';
import Upload from './components/Upload';

export const App: React.FC = () => {
  return (
    <div>
      <Upload />
      <Show />
      {/* <hr/>
      <GreetingView />
      <hr/>
      <AddGreeting />
      <hr/>
      <DeleteGreeting /> */}
    </div>
  );
};
