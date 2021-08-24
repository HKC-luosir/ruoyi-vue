import request from "@/utils/request";

// 查询客户信息列表
export function listCustomer(query) {
  return request({
    url: "/custom/customer/list",
    method: "get",
    params: query
  });
}

// 查询客户信息详细
export function getCustomer(id) {
  return request({
    url: "/custom/customer/" + id,
    method: "get"
  });
}

// 新增客户信息
export function addCustomer(data) {
  return request({
    url: "/custom/customer",
    method: "post",
    data: data
  });
}

// 修改客户信息
export function updateCustomer(data) {
  return request({
    url: "/custom/customer",
    method: "put",
    data: data
  });
}

// 删除客户信息
export function delCustomer(id) {
  return request({
    url: "/custom/customer/" + id,
    method: "delete"
  });
}

// 导出客户信息
export function exportCustomer(query) {
  return request({
    url: "/custom/customer/export",
    method: "get",
    params: query
  });
}

// 查询客户信息详细
export function getCustomerAndSignByPhone(query) {
  return request({
    url: "/custom/customer/getCustomerAndSignByPhone",
    method: "get",
    params: query
  });
}

/**获取用户体征信息 */
export function getCustomerPhysicalSignsByCusId(id) {
  return request({
    url: `/custom/customer/physicalSigns/${id}`,
    method: "get"
  });
}

// 删除客户健康评估信息或体征信息
export function delCustomerHealthy(id) {
  return request({
    url: "/custom/customer/delCustomerHealthy/" + id,
    method: "get"
  });
}
