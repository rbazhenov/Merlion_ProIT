WITH RECURSIVE r AS (
    SELECT id, name, main_organization_id
    FROM organization
    WHERE main_organization_id = 1
    UNION
    SELECT organization.id, organization.name, organization.main_organization_id
    FROM organization
             JOIN r
                  ON organization.main_organization_id = r.id
)
SELECT * FROM r;

select w.id, w.name,w.main_worker_id,(select w2.name
        from worker w2
        where w.main_worker_id = w2.id) as main_worker_id,
        o.name as organization
from worker w
join organization o on w.organization_id = o.id
group by w.id, o.id
order by w.id;

select o.id, o.name,o2.name, w.name, w2.name from Organization o
left join Worker w on o.id = w.organization_id
left join Organization o2 on o.main_organization_id = o2.id
left join Worker w2 on w.main_worker_id = w2.id;

select *
from Organization o
left join Organization o2 on o.main_organization_id = o2.id
order by o.id

