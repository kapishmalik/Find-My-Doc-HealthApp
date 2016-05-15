<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['pwd'])&& isset($_POST['name'])&& isset($_POST['YrsOfExp'])&& isset($_POST['gender'])&& isset($_POST['age'])&& isset($_POST['ugDegree'])&& isset($_POST['pgDegree'])&& isset($_POST['otherDegree'])&& isset($_POST['speciality'])&& isset($_POST['MCINo'])&& isset($_POST['clinicname'])&& isset($_POST['cliniccontact'])&& isset($_POST['clinicaddress'])&& isset($_POST['clinicfees'])){    
    $email        = $_POST['email'];
    $pwd          = md5($_POST['pwd']);
    $name         = $_POST['name'];
    $gender       = $_POST['gender'];
    $YrsOfExp     = $_POST['YrsOfExp'];
    $age          = $_POST['age'];
    $ugDegree     = $_POST['ugDegree'];
    $pgDegree     = $_POST['pgDegree'];
    $otherDegree  = $_POST['otherDegree'];
    $speciality   = $_POST['speciality'];
    $MCINo        = $_POST['MCINo'];
    $clinicname   = $_POST['clinicname'];
    $cliniccontact= $_POST['cliniccontact'];
    $clinicaddress= $_POST['clinicaddress'];
    $clinicfees   = $_POST['clinicfees'];
    $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
mysqli_autocommit($connection,FALSE);
$qualificationid=0;
$clinicid=0;
$sql="SELECT * FROM qualificationdetails order by qualificationid 
DESC LIMIT 1";
$result=mysqli_query($connection,$sql);
if ($result->num_rows > 0) {
  $row = mysqli_fetch_assoc($result);
  $id=$row["qualificationid"]+1;
  $qualificationid=$id;
  $sql="INSERT INTO qualificationdetails values(".$id.",'".$ugDegree."','".$pgDegree."','".$otherDegree."','".$speciality."')";
;
$result1=mysqli_query($connection,$sql);
if(!$result1)
{
  $response["success"] = -1;
  echo json_encode($response);
}
}
else
{
 $id = 1;
 $qualificationid=$id;
  $sql="INSERT INTO qualificationdetails values(".$id.",'".$ugDegree."','".$pgDegree."','".$otherDegree."','".$speciality."')";
$result1=mysqli_query($connection,$sql);
if(!$result1)
{
  $response["success"] = -1;
  echo json_encode($response);
}
}


$sql1="SELECT * FROM clinicdetails order by clinicid 
DESC LIMIT 1";
$result1=mysqli_query($connection,$sql1);
if ($result1->num_rows > 0) {
  $row = mysqli_fetch_assoc($result1);
  $id  = $row["clinicid"]+1;
  $clinicid=$id;
$sql="INSERT INTO clinicdetails values(".$id.",'".$clinicname."','".$clinicaddress."','".$cliniccontact."',".$clinicfees.")";
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
  $sql="INSERT INTO clinicdetails values(".$id.",'".$clinicname."','".$clinicaddress."','".$cliniccontact."',".$clinicfees.")";
$result2=mysqli_query($connection,$sql);
if(!$result2)
{
   $response["success"] = -1;
   echo json_encode($response);
 }
}

$sql3="INSERT into doctor values('".$email."','".$name."','".$gender."',".$age.",".$YrsOfExp.",'".$MCINo."',".$qualificationid.",'".$pwd."')";
$result3=mysqli_query($connection,$sql3);
if(!$result3)
{
  $response["success"] = -1;
  echo json_encode($response);
}

$sql4="INSERT INTO clinicrdoc values(".$clinicid.",'".$email."')";
$result4=mysqli_query($connection,$sql4);
if(!$result4)
{
  $response["success"] = -1;
  echo json_encode($response);
}
else
{
  mysqli_commit($connection);
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

