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
$sql="SELECT * from workingdates where clinicid=".$clinicid." and dates >= '" . date("Y-m-d") . "'";
$result = mysqli_query($connection,$sql);
if($result->num_rows > 0) 
{
$response["success"] = 1;
$response["dates"] = array();
while($row = mysqli_fetch_assoc($result))
{
     $date = array();
     $date["date"] = $row["dates"];
     array_push($response["dates"], $date);
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

