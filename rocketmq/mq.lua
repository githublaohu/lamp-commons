--数据初始化，校验。这里需要一个map集合
--topic@group@broker_hash  
--topic@group@broker_queue  之保存下标
--一个队列，开始执行获得，放到一个集合里面，每定次操作查看这个集合
-- 

local data =nil
local function test()
     redis.log( redis.LOG_NOTICE , "初始化队列id为：")
end

local funName = {
    init = "init" ,
    get  = "get"  ,
    add  = "add"  ,
    deleteConsumption = "deleteConsumption"
}

local operate = {}
setmetatable(_G, operate)
operate.init = function()
  --  需要一个检测数据
  for field , value in pairs( ARGV ) do
    redis.log( redis.LOG_NOTICE , "value : "..value )
    if funName[ value ] == nil then
      redis.log( redis.LOG_NOTICE , "需要初始化的数据是 ： "..value)
      -- topic@group@broker_hash 判断队列id是否存在
      -- 存在什么都不管，不存在初始化
      local messageQueue = cjson.decode( value )
      if messageQueue["queueId"] ~=nil then
        local queueId = redis.call('hsetnx' ,KEYS[1] , messageQueue["queueId"] , cjson.encode( messageQueue ) )
        if queueId == 1 then
            redis.log( redis.LOG_NOTICE , "初始化队列id为：".. messageQueue["queueId"])
            -- 在 topic@group@broker_queue 加入队列
            redis.call("rpush" , KEYS[2] , messageQueue["queueId"])
        end
      else
          redis.log( redis.LOG_WARNING , "messageQueue 数据有问题，问题数据是:"..messageQueue)
          return false
      end
    end
  end
  return true;
end

operate.get=function()
  --时间
  local time = ARGV[2]
  -- 识别 ,二十次识别一次
  local operating = redis.call( 'hget',KEYS[1] , 'operating' )
  redis.log( redis.LOG_NOTICE , "operating：".. tostring( operating ) )
  if operating == false  then
    redis.call( "hset" , KEYS[1] , 'operating', 0 )
    redis.log( redis.LOG_NOTICE , " init operating " )
    operating = 0
  else
    operating = tonumber( operating )
  end
  if operating > 1 then
      local hash = redis.call('hgetall',KEYS[1] )
      for key  , value in ipairs( hash ) do
          local start,endIndex = string.find( value , "time" )
          if start ~= nil and start == 1 then
            local value_time = tonumber( hash[ key+1 ] )
            if value_time ~= nil then
              redis.log( redis.LOG_NOTICE , "key : ".. value .. " time : ".. value_time)
              if time - value_time > 5000 then   -- 判断次队列上次操作，是否超时
                 redis.log( redis.LOG_NOTICE , "chaoshi  : ".. time - value_time)
                -- 重置时间
                redis.call( "hset" , KEYS[1] , value, 0 )
                --重新加入队列
                local id = string.sub(value , 6 , -1)
                redis.log( redis.LOG_NOTICE , "id : " .. id )
                redis.call( "rpush" , KEYS[2] , id  )
              end
            end
          end
      end
      -- 重置操作数，未一
      redis.call( "hset" , KEYS[1] , 'operating', 1 )
  else
    --操作累加  
    redis.call('hincrby',KEYS[1] , 'operating' , 1)
  end
  --  从队列里面获得数据，是否设定超时，超时后，如何处理。 队列设定长度是多少
  local queueId = redis.call("lpop" , KEYS[2] )
  redis.log( redis.LOG_NOTICE , "blpop  : ".. tostring( queueId ) )
  if queueId ~= false then
    --先加入识别
    local timeKey = "time_"..queueId
    redis.call( "hset" , KEYS[1] , timeKey , time )
    --  获得数据之后，获得表示放到哪里
    data = redis.call( "hget" , KEYS[1] , queueId  )
    redis.log( redis.LOG_NOTICE , "data  : ".. tostring( data ) )
    return data
  else
    return ""
  end
end
-- 执行add操作之前一定要保证数据活得到，
operate.add=function()
  local quene = cjson.decode( ARGV[2] )
  
  local offset       = quene["offset"]
  local nextOffset   = quene[ "nextOffset" ]
  local queueId      = quene["queueId"]
  local timeKey      = "time_"..queueId
  
  
  if offset == nextOffset then
    redis.log( redis.LOG_NOTICE , "offset nextOffset equally")
  else
    redis.log( redis.LOG_NOTICE , "offset nextOffset no equally")
    
    
    quene[ 'offset' ]  = nextOffset
    redis.call( "hset" , KEYS[1] , queueId  ,cjson.encode( quene ))
    
    -- 记录消费中
    local time         = quene["time"]
    local consumption  = queueId.."_"..nextOffset.."_"..offset
    -- redis.call( "zadd" , KEYS[3] , time  , consumption)
  end
  redis.log( redis.LOG_NOTICE , "add queueId : "..queueId)
  --  删除表示
  redis.call( "hset" , KEYS[1] , timeKey , 0 )
  --  加入队列
  redis.call("rpush" , KEYS[2] , queueId)
  return true
end


operate.deleteConsumption=function()
  redis.call( "zrem" ,  KEYS[1] , KEYS[2] )
end


operate.checkConsumption=function()
  local time         = ARGV[1]- 1000*60*20
  --ZRANGEBYSCORE 
  local consumption =  redis.call( "zrangebyscore" ,  KEYS[1] ,time , "+inf" , "WITHSCORES"  )
end



local function tableToString( table )
    local str="[";
    for k ,v in pairs(table) do
        str = str.." "..k.." = "..v.." , "
    end
    str=str.."]"
    return str
end
    

redis.log( redis.LOG_NOTICE , "请求方法是： "..tableToString( ARGV ))
redis.log( redis.LOG_NOTICE , "请求KEYS：   "..tableToString( KEYS ))

return operate[ ARGV[1] ]()