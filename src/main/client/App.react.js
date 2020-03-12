import React from 'react';
import {Route} from 'react-router';
import {ApiBase} from './api';
import {Provider} from './api/DocumentTypes';
import {Containers} from '@opuscapita/service-base-ui';
import ParticipantList from './components/ParticipantList';
import ParticipantDetail from "./components/ParticipantDetail";
import CreateParticipant from './components/CreateParticipant';

const home = (props) => (
    <ParticipantList/>
);

const create = (props) => (
    <CreateParticipant/>
);

const detail = (props) => (
    <ParticipantDetail icd={props.params.icd} identifier={props.params.identifier}/>
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
                    <Route path="/detail/:icd/:identifier" component={detail}/>
                </Containers.ServiceLayout>
            </Provider>
        );
    }
}

export default App;
