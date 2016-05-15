<?php
$response = array();

if ( isset($_POST['clinicid']) && isset($_POST['patientEmail']) && isset($_POST['doctorEmail']) && isset($_POST['appointmentDate']) && isset($_POST['appointmentTime']) && isset($_POST['purpose'])&& isset($_POST['slotid']) )
{
$clinicid=$_POST['clinicid'];
$patientEmail=$_POST['patientEmail'];
$docEmail=$_POST['doctorEmail'];

$appointmentDate=$_POST['appointmentDate'];
$slotId=$_POST['slotid'];
$appointmentTime=$_POST['appointmentTime'];
$appointmentStatusCode=0;
$purpose=$_POST['purpose'];

$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
if(!$connection)
{ 
   $response["success"] = -1;
   echo json_encode($response);
}
$sql="INSERT INTO appointments (patientemail,docemail,clinicid,appointmentdate,slotid,appointmenttime,appointmentstatuscode,purpose) VALUES('".$patientEmail."','".$docEmail."',".$clinicid.",'".$appointmentDate."',".$slotId.",'".$appointmentTime."',".$appointmentStatusCode.",'".$purpose."')";

$result = mysqli_query($connection,$sql);
if($result) 
{
$response["success"] = 1;
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

