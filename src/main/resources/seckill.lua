---
--- Generated by Luanalysis
--- Created by Administrator.
--- DateTime: 2024/5/20 14:52
---

--- 参数
--- 优惠券ID
local ticketID = ARGV[1]
--- 用户ID
local userID = ARGV[2]

---
local stockKey = 'seckill:stock:' .. ticketID;
local orderKey = 'seckill:order:' .. ticketID;

if (tonumber(redis.call('get',stockKey))<=0) then
    return 1
end

if (redis.call('sismember',orderKey,userID) == 1) then
    return 2
end

redis.call('incrby',stockKey,-1);
redis.call('sadd',orderKey,userID);

return 0;