<?php
$response = array();
$response["dates"] = array();
if (isset($_POST['email']) && isset($_POST['clinic_name'])){    
    $email =$_POST['email'];
    $clinic =$_POST['clinic_name'];
 $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -2;
     echo json_encode($response);
   }
$sql="select * from clinicrdoc where docemail ='".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
     
     while($row = mysqli_fetch_assoc($result))
     {
     $sql2="select * from clinicdetails where clinicid =".$row["clinicid"]." and name='".$clinic."'";
     $result2=mysqli_query($connection,$sql2);
     if($result2->num_rows > 0)
     {
     $row2 = mysqli_fetch_assoc($result2);
     $sql1="SELECT * FROM workingdates where clinicid = ".$row["clinicid"]." and dates >= '" . date("Y-m-d") . "'";
     $result1=mysqli_query($connection,$sql1);
     
     
	while($row1 = mysqli_fetch_assoc($result1))
	{
	     $datesarr = array();
	     $datesarr["datesarr"] = $row1["dates"];
	     array_push($response["dates"], $datesarr);
	}
     }
     
   } 
     $response["success"] = 1;
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
				