import React from 'react';
import {Containers} from '@opuscapita/service-base-ui';
import ParticipantList from './components/ParticipantList';
import CreateParticipantForm from './components/CreateParticipantForm';

import {Route} from 'react-router';

const home = (props) => (
    <ParticipantList/>
);

const createParticipant = (props) => (
    <CreateParticipantForm/>
);

const App = () => (
    <Containers.ServiceLayout serviceName="peppol-smp">
        <Route path="/" component={home}/>
        <Route path="/create" component={createParticipant}/>
    </Containers.ServiceLayout>
);

export default App;
