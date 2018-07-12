# multi-tenancy-tutorial
Tutorial for about 15 minutes. 

###On multi-tenancy are three levels of isolations:
- tenant per Database
- tenant per schema
- tenant is identify in each tables by special column which store tenantId

###Quick steps
1. Create two databases tenant1db and tenant2db and set users tenant1/tenant1 and tenant2/tenant2
2. Create a table on each databases - set appropriate owner tenant1 and tenant2
 
 ```sql

CREATE TABLE public.team
(
	id bigint,
    name character varying(255) COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.team
    OWNER to tenant1;

```

3. Call POST method: http://localhost:8080/team and set Header appropriate for tenant1 or tenant2: 
X-TenantID: tenant2
 
4. In result should have wrote correct value on correct db.