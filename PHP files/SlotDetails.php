<?php
$response = array();
if (isset($_POST['email'])) 
{
$email=$_POST['email'];
$connection = mysqli_connect('localhost', 'root', '', 'mchealthapp');
if(!$connection)
{ 
   $response["success"] = -1;
   echo json_encode($response);
}

$sql="SELECT * from slots where clinicid in (Select clinicid from clinicrdoc where docemail = '".$email."')";
$result = mysqli_query($connection,$sql);
if($result->num_rows > 0) 
{
$response["success"] = 1;
$response["slot"] = array();
while($row = mysqli_fetch_assoc($result))
{
     $details = array();
	 $details["clinicid"] = $row["clinicid"];
	 $details["slot_id"] = $row["slotid"];
     $details["start_time"]   = $row["starttime"];
     $details["end_time"]  = $row["end_time"];
     array_push($response["slot"], $details);
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

