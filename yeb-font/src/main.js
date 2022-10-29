import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'font-awesome/css/font-awesome.css'

import {postRequest} from "./utils/api";
import {putRequest} from "./utils/api";
import {deleteRequest} from "./utils/api";
import {getRequest} from "./utils/api";
import {initMenu} from "./utils/menus";
import {downloadRequest} from "./utils/download";

//请求方式设置成插件模式,全局this使用
//postRequest后面不能带(),否则不生效
Vue.prototype.postRequest=postRequest;
Vue.prototype.putRequest=putRequest;
Vue.prototype.deleteRequest=deleteRequest;
Vue.prototype.getRequest=getRequest;
Vue.prototype.downloadRequest = downloadRequest;


Vue.config.productionTip = false;
Vue.use(ElementUI);

//路由导航守卫
router.beforeEach((to, from, next)=>{
    //获取token
    if (window.sessionStorage.getItem('tokenStr')){
        initMenu(router,store);
        //判断用户信息是否存在
        if (!window.sessionStorage.getItem('user')){
            return getRequest('/admin/info').then(res=>{
                if (res){
                    //存入用户信息  sessionStorage只能存入字符串   需要转换成字符串
                    window.sessionStorage.setItem('user',JSON.stringify(res));
                    store.commit('UNION_ADMIN',res);
                    next();
                }
            })
        }
        next();
    }else{
        if (to.path === '/') {
            next();
        } else {
            next('/?redirect=' + to.path);
        }

    }
})

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
