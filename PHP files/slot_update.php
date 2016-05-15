<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['clinic_name']) && isset($_POST['starttime']) && isset($_POST['endtime'])){    
    $email =$_POST['email'];
    $clinic_name =$_POST['clinic_name'];
    $starttime =$_POST['starttime'];
    $endtime =$_POST['endtime'];
 $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -2;
     echo json_encode($response);
   }
  $result2 = 0;
  $sql="select * from clinicrdoc where docemail ='".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
    while($row = mysqli_fetch_assoc($result))   
    {
     $sql1="SELECT * FROM clinicdetails where 
      clinicid = ".$row["clinicid"]." and name ='".$clinic_name."'";
     
     $result1=mysqli_query($connection,$sql1);
       if ($result1->num_rows > 0) {
	$row1 = mysqli_fetch_assoc($result1);
        $id = $row1["clinicid"];
	$sql2="INSERT INTO slots (clinicid, starttime, endtime) VALUES('".$id."','".$starttime."','".$endtime."')";
	$result2 = mysqli_query($connection,$sql2);
        if($result2) 
	{
	  $response["success"] = 1;
          echo json_encode($response);
          mysqli_close($connection);
	}
       }
    }	
	$response["success"] = 0;	
	mysqli_close($connection);
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
  $response["success"] = -2;
   echo json_encode($response);
}

?>	