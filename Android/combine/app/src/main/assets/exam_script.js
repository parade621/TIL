var exam_script = {
plus_num: function(num){
try{
var result = num*2
Android.getDoubleNum(result)
}catch(err){
console.long(">> [exam_script.plus_num()] " + err)
}
}
}