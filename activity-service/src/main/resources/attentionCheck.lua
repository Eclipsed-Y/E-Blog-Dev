-- 获取是否已经关注
local hasAttention = redis.call("get", KEYS[1])
local cnt = redis.call("get", KEYS[2])
if hasAttention and cnt then
    local hasAttention_n = tonumber(hasAttention)
    if hasAttention_n == 1 then
        -- 已经关注，取消关注
        cnt = cnt - 1
    else
        cnt = cnt + 1
    end
    redis.call("set", KEYS[1], -hasAttention)
    redis.call("expire", KEYS[1], ARGV[1])
    redis.call("set", KEYS[2], cnt)
    redis.call("expire", KEYS[2], ARGV[2])
end
