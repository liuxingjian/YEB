import {getRequest} from "./api";



export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }

    getRequest("/system/cfg/menu").then(res => {
        if (res) {
            //格式化router
            let fmtRoutes = formatRoutes(res);
            //添加到router
            router.addRoutes(fmtRoutes);
            //将数据存入vuex
            store.commit('initRoutes', fmtRoutes);
            // 连接websocket
            store.dispatch('connect')
        }
    }).catch(e=>{
        console.log(e)
    });


}

export const formatRoutes = (routes) => {
    let fmRoutes = [];
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            meta,
            iconCls,
            children,
        } = router;
        if (children && children instanceof Array) {
            //递归
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            meta: meta,
            iconCls: iconCls,
            children: children,
            component(resolve) {
                if (component.startsWith("Home")){
                    require(['../views/home/' + component + '.vue'], resolve);
                }else if (component.startsWith("Emp")) {
                    require(['../views/emp/' + component + '.vue'], resolve);
                } 
            }
        }
        fmRoutes.push(fmRouter);
    })
    return fmRoutes;
}
