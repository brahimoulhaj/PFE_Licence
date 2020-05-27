<?php 
require('../sihaty/ConnectDB.php');
try{
$specialite = $_POST["specialite"];
$description = $_POST["description"];
$cin = $_POST["cin"];

$pat_info = array();
$data = $conn->query("select * from patients where cin='$cin'");
while($pat = $data->fetch(PDO::FETCH_ASSOC)){
	$pat_info["id"] = $pat["id"];
}

$pat_id = $pat_info["id"];

	
$test = $conn->query("select count(*) as nbr from dossiers where specialite='$specialite' and patient_id=$pat_id");
while($count = $test->fetch(PDO::FETCH_ASSOC)){
	$pat_info["count"] = $count["nbr"];
}
if($pat_info["count"] !== "0"){
	echo "se dossiers est déjà existe!!!";
	$response["success"] = false;
}else{
	$conn->exec("INSERT INTO `dossiers`(`description`, `specialite`, `patient_id`) VALUES (\"$description\",'$specialite',$pat_id)");
	$response["success"] = true;
}
	
}catch(PDOException $e){
	echo "error::::   ".$e;
}

?>


<?php
if($response["success"] == true){
?>
<script>
	window.location.href = "Dossier.php";
</script>
<?php
}else{
	?>
<button onClick="window.history.back();">Retour</button>
<?php
}
?>