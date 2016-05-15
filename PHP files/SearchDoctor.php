<?php
$response = array();
if (isset($_POST['speciality']) && isset($_POST['location'])) 
{
$speciality=$_POST['speciality'];
$location=$_POST['location'];
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="select d.email,d.name,q.speciality,c.fees from doctor d join qualificationdetails q join clinicrdoc cd join clinicdetails c on d.qualificationid = q.qualificationid and d.email = cd.docemail and c.clinicid = cd.clinicid WHERE q.speciality='".$speciality."' and (c.address = '".$location."' or c.address like '%".$location."%')";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
   $response["success"] = 1;
   $response["doctor"] = array();
   while($row = mysqli_fetch_assoc($result)){
   $details = array();
   $details["email"]= $row["email"];
   $details["name"]= $row["name"];
   $details["speciality"]= $row["speciality"];
   $details["fees"]= $row["fees"];
   array_push($response["doctor"], $details);
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
