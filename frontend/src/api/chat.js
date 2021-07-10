import axios from '@/api'

const bringUpChatUser = () => {
    return axios.get("/chat");
};

const bringUpMessageRecord = (getUid, selectedUser) => {
    return axios.get(`/recallChat/${getUid}/${selectedUser}` );
};

const saveReportInfo = (getUid, selectedUser, picked, reportDetail) => {
    
    return axios.post('/report', {
        reportId : getUid,
        reportedId : selectedUser,
        reportType : picked,
        reportDetail : reportDetail
    });
};

export default {
    bringUpChatUser,
    bringUpMessageRecord,
    saveReportInfo,
};