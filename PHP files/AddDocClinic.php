<?php
$response = array();
if (isset($_POST['docemail']) && isset($_POST['clinic_name']) && isset($_POST['clinic_contact']) && isset($_POST['clinic_address']) && isset($_POST['clinic_fees'])){    
    $docemail = $_POST['docemail'];
    $clinicname   = $_POST['clinic_name'];
    $cliniccontactno= $_POST['clinic_contact'];
    $clinicaddress= $_POST['clinic_address'];
    $clinicfees   = $_POST['clinic_fees'];
    $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql1="SELECT * FROM clinicdetails order by clinicid 
DESC LIMIT 1";
$result1=mysqli_query($connection,$sql1);
if ($result1->num_rows > 0) {
  $row = mysqli_fetch_assoc($result1);
  $id  = $row["clinicid"]+1;
  $clinicid=$id;
$sql="INSERT INTO clinicdetails values(".$id.",'".$clinicname."','".$clinicaddress."','".$cliniccontactno."',".$clinicfees.")";
$result2=mysqli_query($connection,$sql);
if(!$result2)
{
   $response["success"] = -1;
   echo json_encode($response);
 }
}
else
{
  $id=1;
  $clinicid=$id;
  $sql="INSERT INTO clinicdetails values(".$id.",'".$clinicname."','".$clinicaddress."','".$cliniccontactno."',".$clinicfees.")";
$result2=mysqli_query($connection,$sql);
if(!$result2)
{
   $response["success"] = -1;
   echo json_encode($response);
 }
}
$sql4="INSERT INTO clinicrdoc values(".$clinicid.",'".$docemail."')";
$result4=mysqli_query($connection,$sql4);
if(!$result4)
{
  $response["success"] = -1;
  echo json_encode($response);
}
else
{
  $response["success"] = 1;
  echo json_encode($response);
}

mysqli_close($connection);
}
else
{
  $response["success"] = -1;
   echo json_encode($response);
}

?>

