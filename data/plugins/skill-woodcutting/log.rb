LOGS = {}

EXPIRED_LOGS = {}

# Only normal trees support atm

class Log
 attr_reader :id, :objects, :level, :exp, :respawn

def initialize(id, objects, level, exp, respawn)
  @id = id
  @objects = objects
  @level = level
  @exp = exp
  @respawn = respawn
 end
end

def append_log(log)
 log.objects.each do |obj, expired_obj|
 LOGS[obj] = log
 EXPIRED_LOGS[expired_obj] = true
 end
end


NORMAL_TREE = {
 1315 => 1342, 1316 => 1342, 1318 => 1342, 1319 => 1342,
 3033 => 1342, 1278 => 1342, 1276 => 1342, 1286 => 1342,
 1282 => 1342, 1383 => 1342, 1286 => 1342,
}

append_log Log.new(1511, NORMAL_TREE, 1, 25, 3) # normal log