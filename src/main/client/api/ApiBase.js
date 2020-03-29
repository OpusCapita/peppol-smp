import request from 'superagent';
import ApiError from './ApiError';

class ApiBase {

    ajax = request;

    getParticipantList(pagination, filter) {
        return this.ajax.post(`/peppol-smp/api/get-participants`).send({pagination, filter}).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getDocumentTypes() {
        return this.ajax.get(`/peppol-validator/api/public/get-document-types`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    bulkRegister(request) {
        return this.ajax.post(`/peppol-smp/api/bulk-register`).send(request).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    addParticipant(participant) {
        return this.ajax.post(`/peppol-smp/api/add-participant`).send(participant).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    deleteParticipant(id) {
        return this.ajax.post(`/peppol-smp/api/delete-participant/${id}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getParticipantDetail(icd, identifier) {
        return this.ajax.get(`/peppol-smp/api/get-participant/${icd}/${identifier}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getParticipantDetailById(id) {
        return this.ajax.get(`/peppol-smp/api/get-participant-by-id/${id}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }
}

export default ApiBase;
