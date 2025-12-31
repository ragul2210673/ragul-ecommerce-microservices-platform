\# Configuration Guide - Spring Ecommerce



\## Overview

This guide explains how to configure the system for different business requirements without code changes.



---



\## Configuration Management



\### Accessing Configuration API



\*\*Endpoint\*\*: `http://localhost:8084/api/admin/config`



\### Configuration Structure

```json

{

&nbsp; "configKey": "order.minimum.amount",

&nbsp; "configValue": "50.00",

&nbsp; "configType": "NUMBER",

&nbsp; "description": "Minimum order amount allowed",

&nbsp; "module": "ORDER",

&nbsp; "isActive": true

}

