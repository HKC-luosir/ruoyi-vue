import request from '@/utils/request'

// 查询微信用户记录列表
export function listWxUserLog(query) {
  return request({
    url: '/custom/wxUserLog/list',
    method: 'get',
    params: query
  })
}

// 查询微信用户记录详细
export function getWxUserLog(openid) {
  return request({
    url: '/custom/wxUserLog/' + openid,
    method: 'get'
  })
}

// 新增微信用户记录
export function addWxUserLog(data) {
  return request({
    url: '/custom/wxUserLog',
    method: 'post',
    data: data
  })
}

// 修改微信用户记录
export function updateWxUserLog(data) {
  return request({
    url: '/custom/wxUserLog',
    method: 'put',
    data: data
  })
}

// 删除微信用户记录
export function delWxUserLog(id) {
  return request({
    url: '/custom/wxUserLog/' + id,
    method: 'delete'
  })
}

// 导出微信用户记录
export function exportWxUserLog(query) {
  return request({
    url: '/custom/wxUserLog/export',
    method: 'get',
    params: query
  })
}

// 根据ID查询打卡日志详情
export function getPunchLogDetail(id) {
  return request({
    url: '/custom/wxUserLog/getPunchLogDetail/' + id,
    method: 'get',
    params: {}
  })
}

// 根据用户ID查询打卡日志详情
export function getAllPunchLogByCustomerId(query) {
  return request({
    url: '/custom/wxUserLog/getAllPunchLogByCustomerId',
    method: 'get',
    params: query
  })
}

// 点评打卡
export function commentPunchContent(data) {
  return request({
    url: '/custom/wxUserLog/commentPunchContent',
    method: 'post',
    data: data
  })
}

// 查询未打卡用户列表
export function getNotPunchCustomer(query) {
  return request({
    url: '/custom/wxUserLog/selectNotPunchCustomerByDate',
    method: 'get',
    params: query
  })
}






