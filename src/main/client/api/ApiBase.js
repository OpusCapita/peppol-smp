import request from 'superagent';
import ApiError from './ApiError';

class ApiBase {

    ajax = request;

    getLookup(icd, identifier) {
        return this.ajax.get(`/peppol-smp/api/lookup/${icd}/${identifier}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getParticipantList(pagination, filter) {
        return this.ajax.post(`/peppol-smp/api/get-participants`).send({pagination, filter}).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getDocumentTypes(icd) {
        return this.ajax.get(`/peppol-smp/api/get-document-types/${icd}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    addParticipant(participant) {
        return this.ajax.post(`/peppol-smp/api/add-participant`).send(participant).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }
}

export default ApiBase;
