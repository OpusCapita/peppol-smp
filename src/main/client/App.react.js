import React from 'react';
import {Route} from 'react-router';
import {ApiBase} from './api';
import {Provider} from './api/DocumentTypes';
import {Containers} from '@opuscapita/service-base-ui';
import BulkRegister from "./components/BulkRegister";
import ParticipantList from './components/ParticipantList';
import OperationHistory from './components/OperationHistory';
import ParticipantDetail from './components/ParticipantDetail';
import CreateParticipant from './components/CreateParticipant';
import LookupParticipant from './components/LookupParticipant';

const home = (props) => (
    <ParticipantList/>
);

const create = (props) => (
    <CreateParticipant/>
);

const edit = (props) => (
    <CreateParticipant id={props.params.id}/>
);

const bulkRegister = (props) => (
    <BulkRegister/>
);

const operationHistory = (props) => (
    <OperationHistory/>
);

const detail = (props) => (
    <ParticipantDetail icd={props.params.icd} identifier={props.params.identifier}/>
);

const lookup = (props) => (
    <LookupParticipant />
);

class App extends React.Component {

    state = {
        documentTypes: [],
    };

    constructor(props) {
        super(props);
        this.api = new ApiBase();
    }

    componentDidMount() {
        this.api.getDocumentTypes().then(documentTypes => {
            this.setState({documentTypes: documentTypes});
        }).catch(e => {
            this.context.showNotification(e.message, 'error', 10);
        });
    }

    render() {
        return (
            <Provider value={this.state}>
                <Containers.ServiceLayout serviceName="peppol-smp">
                    <Route path="/" component={home}/>
                    <Route path="/create" component={create}/>
                    <Route path="/edit/:id" component={edit}/>
                    <Route path="/bulkRegister" component={bulkRegister}/>
                    <Route path="/detail/:icd/:identifier" component={detail}/>
                    <Route path="/lookup" component={lookup}/>
                    <Route path="/operationHistory" component={operationHistory}/>
                </Containers.ServiceLayout>
            </Provider>
        );
    }
}

export default App;
