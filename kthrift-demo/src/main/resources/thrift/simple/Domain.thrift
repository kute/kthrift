namespace java com.kute.thrift.simple.domain

include "Types.thrift"

struct UserInfo {

    /**
    * 用户ID
    **/
   1:required Types.Integer  id = 1;

    /**
    * 用户登陆名
    **/
   2:required Types.String  login;

    /**
    * 用户名字
    **/
   3:required Types.String   name;

    /**
    * 用户电话
    **/
   4:required Types.String   mobile;

   5: Types.Boolean isAdmin;

   6: optional string location
}

struct ResponseCode {

    1: required Types.Integer code,

    2: required Types.String message

}