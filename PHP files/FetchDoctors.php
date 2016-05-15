<?php
$response = array();
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
if(!$connection)
{ 
   $response["success"] = -1;
   echo json_encode($response);
}
$sql="SELECT * from doctor";
$result = mysqli_query($connection,$sql);
if($result->num_rows > 0) 
{
$response["success"] = 1;
$response["doctor"] = array();
while($row = mysqli_fetch_assoc($result))
{
     $details = array();
     $details["name"]   = $row["name"];
     $details["email"]  = $row["email"];
     array_push($response["doctor"], $details);
}
}
else
{
  $response["success"] = 0;
}
echo json_encode($response);
mysqli_close($connection);
?>

