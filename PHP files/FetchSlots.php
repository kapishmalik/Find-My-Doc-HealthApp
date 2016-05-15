<?php
$response = array();
if (isset($_POST['clinicid']))
{
$clinicid = $_POST['clinicid'];
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
if(!$connection)
{ 
   $response["success"] = -1;
   echo json_encode($response);
}
$sql="SELECT * from slots where clinicid=".$clinicid." order by starttime";
$result = mysqli_query($connection,$sql);
if($result->num_rows > 0) 
{
$response["success"] = 1;
$response["slots"] = array();
while($row = mysqli_fetch_assoc($result))
{
     $slot = array();
     $slot["slotId"] = $row["slotid"];
     $slot["startTime"] = $row["starttime"];
     $slot["endTime"] = $row["endtime"];
     array_push($response["slots"], $slot);
}
}
else
{
  $response["success"] = 0;
}
echo json_encode($response);
mysqli_close($connection);   
}
else
{
   $response["success"] = -1;
   echo json_encode($response);

}

?>


