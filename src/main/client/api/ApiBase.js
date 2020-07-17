import request from 'superagent';
import ApiError from './ApiError';

class ApiBase {

    ajax = request;

    getParticipantList(pagination, filter) {
        return this.ajax.post(`/peppol-smp/api/get-participants`).send({pagination, filter}).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getOperationHistory(pagination, filter) {
        return this.ajax.post(`/peppol-smp/api/get-operation-history`).send({pagination, filter}).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getDocumentTypes() {
        return this.ajax.get(`/peppol-validator/api/public/get-document-types`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    bulkRegister(request, userId) {
        return this.ajax.post(`/peppol-smp/api/bulk-register/${userId}`).send(request).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    addParticipant(participant, userId) {
        return this.ajax.post(`/peppol-smp/api/add-participant/${userId}`).send(participant).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    editParticipant(participant, userId) {
        return this.ajax.post(`/peppol-smp/api/edit-participant/${userId}`).send(participant).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    deleteParticipant(id, userId) {
        return this.ajax.post(`/peppol-smp/api/delete-participant/${userId}/${id}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getParticipantDetail(icd, identifier) {
        return this.ajax.get(`/peppol-smp/api/get-participant/${icd}/${identifier}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    getParticipantDetailById(id) {
        return this.ajax.get(`/peppol-smp/api/get-participant-by-id/${id}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    lookupParticipant(icd, identifier) {
        return this.ajax.get(`/peppol-outbound/api/public/lookup/${icd}/${identifier}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }

    clearAPParticipantCache(icd, identifier) {
        return this.ajax.get(`/peppol-processor/api/public/clear-cache/${icd}/${identifier}`).then(res => res.body).catch(ApiError.getErrorFromResponse);
    }
}

export default ApiBase;
