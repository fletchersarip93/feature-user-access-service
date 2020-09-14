# Feature User Access API
API to manage users’ accesses to new features via feature switches, i.e. enabling/disabling certain feature based on a user’s email and feature names).

## Version: 1.0.0

**Contact information:**  
fletchersarip@gmail.com  

**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

### /feature

#### GET
##### Summary

get access permission of a user for a feature

##### Description

By passing in the user's email and feature's name, you can check whether
the user can access the specified feature.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| email | query | user's email (case-insensitive) | Yes | string (email) |
| featureName | query | name of the feature (case-insensitive) | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | information on whether the specified user is allowed access for the specified feature |
| 400 | bad input parameter |
| 404 | the specified user or feature cannot be found |
| 500 | internal server error |

#### POST
##### Summary

configure a user's access for a feature

##### Description

By passing in a user's email, a feature's name, and a boolean value on whether to give access or not,
you can configure the user's access to the specified feature.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | user-feature access succesfully configured |
| 304 | not modified |
| 400 | bad input parameter |
| 500 | internal server error |
