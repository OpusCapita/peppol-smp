import React from 'react';
import {Containers} from '@opuscapita/service-base-ui';
import PeppolSmp from './components/PeppolSmp';
import ParticipantList from './components/ParticipantList';

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

const App = () => (
    <Containers.ServiceLayout serviceName="peppol-smp">
        <Route path="/" component={home}/>
        <Route path="/participants" component={participantList}/>
    </Containers.ServiceLayout>
);

export default App;
