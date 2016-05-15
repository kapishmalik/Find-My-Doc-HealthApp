<?php
$response = array();
if(isset($_POST['email']))
{    
   $email  =$_POST['email'];
   $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="select d.name,q.speciality,c.address,c.contactno,q.ugdegree,q.pgdegree,q.otherdegree,d.yrsofexp from doctor d join qualificationdetails q join clinicrdoc cd join clinicdetails c on d.qualificationid = q.qualificationid and d.email = cd.docemail and c.clinicid = cd.clinicid WHERE d.email = '".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
    $row = mysqli_fetch_assoc($result);
    $response["success"] = 1;
    $response["name"]= $row["name"];
    $response["speciality"]= $row["speciality"];
    $response["address"]= $row["address"];
    $response["contactno"]= $row["contactno"];  
    $response["ugdegree"]= $row["ugdegree"];
    $response["pgdegree"]= $row["pgdegree"];
    $response["otherdegree"]= $row["otherdegree"];
    $response["yrsofexp"]= $row["yrsofexp"];
    echo json_encode($response);
   }    
   else
  { 
   $response["success"] = 0;
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

