import request from '@/utils/request'

// 查询园历管理(班级)列表
export function listSchoolcalendarclass(query) {
    return request({
        url: '/benyi/schoolcalendarclass/list',
        method: 'get',
        params: query
    })
}

// 不分页查询班级列表
export function listSchoolcalendarclassAll(query) {
    return request({
        url: '/benyi/schoolcalendarclass/listAll',
        method: 'get',
        params: query
    })
}

// 用于显示到首页的园历信息查询 暂时不写备用状态
// export function getSchoolCalendarsClass(query) {
//     return request({
//         url: '/benyi/schoolcalendarclass/getSchoolCalendarsClass',
//         method: 'get',
//         params: query
//     })
// }

// 查询园历管理(班级)详细
export function getSchoolcalendarclass(id) {
    return request({
        url: '/benyi/schoolcalendarclass/' + id,
        method: 'get'
    })
}

// 新增园历管理(班级)
export function addSchoolcalendarclass(data) {
    return request({
        url: '/benyi/schoolcalendarclass',
        method: 'post',
        data: data
    })
}

// 修改园历管理(班级)
export function updateSchoolcalendarclass(data) {
    return request({
        url: '/benyi/schoolcalendarclass',
        method: 'put',
        data: data
    })
}

// 删除园历管理(班级)
export function delSchoolcalendarclass(id) {
    return request({
        url: '/benyi/schoolcalendarclass/' + id,
        method: 'delete'
    })
}

// 导出园历管理(班级)
export function exportSchoolcalendarclass(query) {
    return request({
        url: '/benyi/schoolcalendarclass/export',
        method: 'get',
        params: query
    })
}