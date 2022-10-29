import axios from "axios";
import {Message} from "element-ui";
import router from "../router";

const service = axios.create({
    responseType: "arraybuffer"
})

service.interceptors.request.use(config => {
    config.headers['Authorization']=window.sessionStorage.getItem('tokenStr');
        return config;
    },error => {
        console.log(error)
})

service.interceptors.response.use(res => {
    const headers = res.headers;
    let reg = RegExp('/application\/json/')
    if (headers['content-type'].match(reg)){
        res.data = unitToString(res.data)
    }else {
        let fileDownload = require('js-file-download')
        let fileName = headers['content-disposition'].split(';')[1].split('filename=')[1]
        let contentType = headers['content-type']
        fileName = decodeURIComponent(fileName)
        fileDownload(res.data,fileName,contentType)
    }

}, error => {
    console.log(error)
});

function unitToString(unitArray) {
    let encodedString = String.fromCharCode.apply(null,new Uint8Array(unitArray))
    let decodedString = decodeURIComponent(escape(encodedString));
    return JSON.parse(decodedString)
}

let base = ''
export const downloadRequest=(url,params) => {
    return service({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}