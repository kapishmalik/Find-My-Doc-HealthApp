<?php
$response = array();
if (isset($_POST['patientemail'])) 
{
$patientemail=$_POST['patientemail'];
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="SELECT a.appointmentid,d.name,d.email,c.clinicid,a.appointmentdate,a.appointmenttime, a.appointmentstatuscode,a.purpose,c.address from appointments a join clinicdetails c join doctor d on a.clinicid = c.clinicid and a.docemail = d.email WHERE a.patientemail = '".$patientemail."' and (a.appointmentstatuscode = 0 or a.appointmentstatuscode = 1 or a.appointmentstatuscode = 2) and a.appointmentdate >= CURDATE()";
  $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
   $response["success"] = 1;
   $response["appointments"] = array();
   while($row = mysqli_fetch_assoc($result)){
   $details = array();
   $details["appointmentid"]= $row["appointmentid"];
   $details["name"]= $row["name"];
$details["email"]= $row["email"];
   $details["clinicid"]= $row["clinicid"];
   $details["appointmentdate"]= $row["appointmentdate"];
   $details["appointmenttime"]= $row["appointmenttime"];
   $details["appointmentstatuscode"]= $row["appointmentstatuscode"];
   $details["purpose"]= $row["purpose"];
   $details["address"]= $row["address"];
   array_push($response["appointments"], $details);
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
	