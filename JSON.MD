
> 7 span data include

```
[
  {
    "traceId": "c9ff8064b1958326",
    "id": "c9ff8064b1958326",
    "name": "buy",
    "timestamp": 1499330001114952,
    "duration": 15959,
    "annotations": [
      {
        "timestamp": 1499330001114952,
        "value": "cs",
        "endpoint": {
          "serviceName": "consumer-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001115095,
        "value": "sr",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001130911,
        "value": "cr",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001131048,
        "value": "ss",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "0ms",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buy",
          "ipv4": "10.82.12.33",
          "port": 20880
        }
      },
      {
        "key": "总耗时",
        "value": "12ms",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "10ms",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  },
  {
    "traceId": "c9ff8064b1958326",
    "id": "76942e24f7c02295",
    "name": "buya",
    "parentId": "c9ff8064b1958326",
    "timestamp": 1499330001115923,
    "duration": 13370,
    "annotations": [
      {
        "timestamp": 1499330001115923,
        "value": "cs",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001116197,
        "value": "sr",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001128643,
        "value": "ss",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001129293,
        "value": "cr",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "0ms",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buya",
          "ipv4": "10.82.12.33",
          "port": 20881
        }
      },
      {
        "key": "总耗时",
        "value": "4ms",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "3ms",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  },
  {
    "traceId": "c9ff8064b1958326",
    "id": "62cbb3430ea346f6",
    "name": "buyb",
    "parentId": "c9ff8064b1958326",
    "timestamp": 1499330001128796,
    "duration": 1571,
    "annotations": [
      {
        "timestamp": 1499330001128796,
        "value": "cs",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001129155,
        "value": "sr",
        "endpoint": {
          "serviceName": "consumer-buyb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001129940,
        "value": "ss",
        "endpoint": {
          "serviceName": "consumer-buyb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001130367,
        "value": "cr",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "consumer-buyb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buyb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buyb",
          "ipv4": "10.82.12.33",
          "port": 20881
        }
      },
      {
        "key": "总耗时",
        "value": "2ms",
        "endpoint": {
          "serviceName": "provider-buy",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "consumer-buyb",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  },
  {
    "traceId": "c9ff8064b1958326",
    "id": "4b5fc644d4f7da1c",
    "name": "buyaa",
    "parentId": "76942e24f7c02295",
    "timestamp": 1499330001117033,
    "duration": 1601,
    "annotations": [
      {
        "timestamp": 1499330001116768,
        "value": "sr",
        "endpoint": {
          "serviceName": "provider-buyaa",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001117033,
        "value": "cs",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001117537,
        "value": "ss",
        "endpoint": {
          "serviceName": "provider-buyaa",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001118634,
        "value": "cr",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "0ms",
        "endpoint": {
          "serviceName": "provider-buyaa",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buyaa",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buyaa",
          "ipv4": "10.82.12.33",
          "port": 20880
        }
      },
      {
        "key": "总耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "provider-buyaa",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  },
  {
    "traceId": "c9ff8064b1958326",
    "id": "627d169c92d08ac2",
    "name": "buyab",
    "parentId": "76942e24f7c02295",
    "timestamp": 1499330001118877,
    "duration": 9364,
    "annotations": [
      {
        "timestamp": 1499330001118877,
        "value": "cs",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001120741,
        "value": "sr",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001128241,
        "value": "cr",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001128284,
        "value": "ss",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "2ms",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buyab",
          "ipv4": "10.82.12.33",
          "port": 20880
        }
      },
      {
        "key": "总耗时",
        "value": "10ms",
        "endpoint": {
          "serviceName": "consumer-buya",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "7ms",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  },
  {
    "traceId": "c9ff8064b1958326",
    "id": "b2c9e41caf469e2d",
    "name": "buyab",
    "parentId": "627d169c92d08ac2",
    "timestamp": 1499330001122786,
    "duration": 1692,
    "annotations": [
      {
        "timestamp": 1499330001122786,
        "value": "cs",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001122928,
        "value": "sr",
        "endpoint": {
          "serviceName": "consumer-buyaba",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001123130,
        "value": "ss",
        "endpoint": {
          "serviceName": "consumer-buyaba",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001124478,
        "value": "cr",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "0ms",
        "endpoint": {
          "serviceName": "consumer-buyaba",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buyaba",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buyab",
          "ipv4": "10.82.12.33",
          "port": 20881
        }
      },
      {
        "key": "总耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "consumer-buyaba",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  },
  {
    "traceId": "c9ff8064b1958326",
    "id": "844feb6ae24f5019",
    "name": "buyab",
    "parentId": "627d169c92d08ac2",
    "timestamp": 1499330001124884,
    "duration": 3349,
    "annotations": [
      {
        "timestamp": 1499330001124884,
        "value": "cs",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001125452,
        "value": "sr",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001127354,
        "value": "ss",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "timestamp": 1499330001128233,
        "value": "cr",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      }
    ],
    "binaryAnnotations": [
      {
        "key": "c-p网络耗时",
        "value": "1ms",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "consumer响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "provider响应状态",
        "value": "OK",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "sa",
        "value": true,
        "endpoint": {
          "serviceName": "buyab",
          "ipv4": "10.82.12.33",
          "port": 20881
        }
      },
      {
        "key": "总耗时",
        "value": "3ms",
        "endpoint": {
          "serviceName": "provider-buyab",
          "ipv4": "192.168.186.1"
        }
      },
      {
        "key": "远程方法耗时",
        "value": "2ms",
        "endpoint": {
          "serviceName": "consumer-buyabb",
          "ipv4": "192.168.186.1"
        }
      }
    ]
  }
]

```