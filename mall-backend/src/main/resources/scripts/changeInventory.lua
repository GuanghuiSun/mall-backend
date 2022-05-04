-- 比较库存是否充足
local inventory = redis.call('get',KEYS[1])
if(inventory >= ARGV[1]) then
	-- 库存充足 削减库存
	local new = inventory - ARGV[1]
	redis.call('set',KEYS[1],new)
	return 1
end
return 0