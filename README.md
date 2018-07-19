# multi-tenancy-tutorial
Tutorial for about 15 minutes. I recommend to use postgresql database.

### On multi-tenancy are three levels of isolation:
- tenant per Database
- tenant per schema
- tenant is identify in each tables by special column which store tenantId

### Quick steps
1.Create default database "default_tenant" which is needed to run application. User/password: default_tenant/default_tenant
 
2.Create two databases: tenant1db with user/password "tenant1/tenant1" and tenant2db with user/password "tenant2/tenant2"

3.Create a table on tenant1db and tenant2db databases - set appropriate owner tenant1 and tenant2.
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
    OWNER to tenant2;
```
4.Call POST method on endpoint http://localhost:8080/team . On header set appropriate value for tenant1 or tenant2: 
X-TenantID: tenant2
 
5.Check results in tables. Depends on X-TenantID data will persist in tenat1db or tenant2db.

### Appendix
- Information about tenants database are stored in files in directory /tenants.
- To switch isolation to schema name isolation, you must change database name in files stored in folder /tenants and add correct schemas on database. 
- class TenantDataSource thanks to extends AbstractRoutingDataSource override method which get current tenant and in runtime get correct database. In TenantConfiguration I prepare TenantDataSource with default database and two extra databases - tenant1db and tenant2db. 
 
