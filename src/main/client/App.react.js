import React from 'react';
import {Containers} from '@opuscapita/service-base-ui';
import PeppolSmp from './components/PeppolSmp';
import ParticipantList from './components/ParticipantList';
// import CreateParticipantForm from './components/CreateParticipantForm';

import {Route} from 'react-router';

const menuButton = (router) => (
    <div className="footer-wrapper">
        <a className='btn btn-default' href="#" onClick={() => router.push('/peppol-smp/')}>
            <span className="icon glyphicon glyphicon-chevron-left"/> Go to Menu
        </a>
    </div>
);

const home = (props) => (
    <PeppolSmp/>
);

const participantList = (props) => (
    <div>
        <ParticipantList/>
        {menuButton(props.router)}
    </div>
);

// const createParticipant = (props) => (
    {/*<CreateParticipantForm/>*/}
// );

const App = () => (
    <Containers.ServiceLayout serviceName="peppol-smp">
        <Route path="/" component={home}/>
        <Route path="/participants" component={participantList}/>
        {/*<Route path="/newParticipant" component={createParticipant}/>*/}
    </Containers.ServiceLayout>
);

export default App;
