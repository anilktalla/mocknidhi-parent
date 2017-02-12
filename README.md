# Work in Progress


````
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/xml' -d '{
	"active": true,
	"baseURI": "/",
	"contextPath": "/foo-service",
	"description": "test description",
	"httpVersion": "1.2",
	"license": "Apache 2.0",
	"licenseURL": "http://www.apache.org/licenses/LICENSE-2.0",
	"name": "Foo Service",
	"port": 8085,
	"responseDelayInSeconds": 0,
	"version": "1.0.0"
}' 'http://localhost:8080/mocknidhi-service/api/v1/apiDefinition'
````

````
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "active": true,
  "description": "Bar Operation",
  "displayName": "Bar Operation",
  "request": {
    "comment": "Bar comment",
    "headers": [
      {
        "comment": "Header comment",
        "name": "header-name",
        "value": "Zoo"
      }
    ],
    "method": "GET",
    "postData": {
      "mimeType": "string",
      "params": [],
      "text": "{\n \"foo\": \"Hello Word\"\n}"
    },
    "queryParams": [
      {
        "comment": "Query Comment",
        "name": "abc",
        "value": "2"
      }
    ]
  },
  "resourcePath": "/bar",
  "response": {
    "comment": "string",
    "content": {
      "comment": "string",
      "mimeType": "string",
      "text": "{\n \"foo\": \"Hello Word\"\n}"
    },
    "headers": [
      {
        "comment": "string",
        "name": "response-header",
        "value": "blah"
      }
    ],
    "status": 200,
    "statusText": "string"
  },
  "responseDelayInSeconds": 0,
  "verb": "GET"
}' 'http://localhost:8080/mocknidhi-service/api/v1/apiDefinition/fdefa8c0-8257-4ad3-a6bd-847025e33679/operationDefinition'
````