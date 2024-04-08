-- 获取是否已经点赞
local liked = redis.call("get", KEYS[1])
local cnt = redis.call("get", KEYS[2])
if liked and cnt then
    local liked_n = tonumber(liked)
    if liked_n == 1 then
        -- 已经点赞，取消点赞
        cnt = cnt - 1
    else
        cnt = cnt + 1
    end
    redis.call("set", KEYS[1], -liked)
    redis.call("expire", KEYS[1], ARGV[1])
    redis.call("set", KEYS[2], cnt)
    redis.call("expire", KEYS[2], ARGV[2])
end
