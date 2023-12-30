
-- solution 1
with table1 as (
select t.request_at,t.status
from Trips t
left join Users u on t.client_id = u.users_id
left join Users u2 on t.driver_id = u2.users_id
where u.banned = 'No' and u2.banned = 'No' and t.request_at between '2013-10-01' and '2013-10-03'
order by t.request_at,t.client_id,t.driver_id
)
select
      t.request_at as "Day",
      round(count(case when t.status = 'cancelled_by_driver' or t.status = 'cancelled_by_client' then 1 end)::numeric
    /
    COUNT(t.request_at) ,2)AS "Cancellation Rate"

from table1 t
group by t.request_at;

-- solution 2
-- Write your PostgreSQL query statement below
select
        request_at as "Day",
        round(count(case when status != 'completed' then status else null end)::numeric / COUNT(*), 2) as "Cancellation Rate"
FROM Trips
INNER JOIN Users client ON client.users_id = Trips.client_id
INNER JOIN Users driver ON driver.users_id = Trips.driver_id
WHERE client.banned = 'No' AND driver.banned = 'No' AND request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY request_at