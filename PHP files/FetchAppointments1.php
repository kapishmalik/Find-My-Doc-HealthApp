<?php
$response = array();
$clinicid = 3;
$appointmentDate = "2015-12-01";
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
if(!$connection)
{ 
   $response["success"] = -1;
   echo json_encode($response);
}
$sql="SELECT * from appointments where clinicid=".$clinicid." and appointmentdate='".$appointmentDate."' and (appointmentstatuscode=0 or appointmentstatuscode=1) order by appointmenttime";
$result = mysqli_query($connection,$sql);
if($result->num_rows > 0) 
{
$response["success"] = 1;
$response["appointments"] = array();
while($row = mysqli_fetch_assoc($result))
{
     $time = array();
     $time["time"] = $row["appointmenttime"];
     array_push($response["appointments"], $time);
}
}
else
{
  $response["success"] = 0;
}
echo json_encode($response);
mysqli_close($connection);
 
?>