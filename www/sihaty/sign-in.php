<?php

require('../sihaty/ConnectDB.php');
if(!empty($_POST["fullname"]) || !empty($_POST["cin"]) || !empty($_POST["naissance"]) || !empty($_POST["telephone"]) || !empty($_POST["sexe"])){
	
        $fullname = $_POST["fullname"];
        $cin = $_POST["cin"];
        $naissance = $_POST["naissance"];
        $telephone = $_POST["telephone"];
        $sexe = $_POST["sexe"];
				$email = $_POST["email"];
				$adresse = $_POST["adresse"];
				//$photo = basename($_FILES["photo"]["name"]);
	
				
        
        if(strlen($telephone) <> 10 || $telephone[0] <> '0' || ($telephone[1] <> '6' && $telephone[1] <> '7') ){
            echo "ce N° de téléphone est invalide\n";
        }else{
            if(strlen($cin) > 10 || strlen($cin) < 5){
                echo "ce N° de cin est invalide\n";
            }else{
                $test_cin = $conn->query("SELECT * FROM patients WHERE cin='$cin'");
                $count = 0;
                while($donnee = $test_cin->fetch()){ $count++; }
                if($count > 0 ){
                    echo "ce N° de CIN est déjà utiliser\n";
                }else{
                    $test_cin = $conn->query("SELECT * FROM patients WHERE telephone='$telephone'");
                    $count = 0;
                    while($donnee = $test_cin->fetch()){ $count++; }
                    if($count > 0 ){
                        echo "ce N° de téléphone est déjà utiliser\n";
                    }else{
                    $test_cin = $conn->query("SELECT * FROM patients WHERE email='$email'");
                    $count = 0;
                    while($donnee = $test_cin->fetch()){ $count++; }
                    if($count > 0 ){
											echo "cet email est déjà utiliser\n";
                    }else{
                        $response["success"] = TRUE;
                    }
                }
            }
        }
    }
				/*$target_dir = "../sihaty/img/";
				$target_file = $target_dir . basename($_FILES["photo"]["name"]);
				$uploadOk = 1;
				$imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

				$check = getimagesize($_FILES["photo"]["tmp_name"]);
				if($check !== false) {
						echo "File is an image - " . $check["mime"] . ".";
						$uploadOk = 1;
				} else {
						echo "File is not an image.";
						$uploadOk = 0;
				}
				// Check file size
				if ($_FILES["photo"]["size"] > 1000000) {
						echo "Sorry, your file is too large.\n";
						$uploadOk = 0;
				}
				// Allow certain file formats
				if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
				&& $imageFileType != "JPG" && $imageFileType != "PNG" && $imageFileType != "JPEG" ) {
						echo "Sorry, only JPG, JPEG, PNG & GIF files are allowed.\n";
						$uploadOk = 0;
				}
				// Check if $uploadOk is set to 0 by an error
				if ($uploadOk == 0) {
					$response["success"] = false;
						echo "Sorry, your file was not uploaded.\n";
				// if everything is ok, try to upload file
				} else {
						if (move_uploaded_file($_FILES["photo"]["tmp_name"], $target_file)) {
								echo "The file ". basename( $_FILES["photo"]["name"]). " has been uploaded.";
						} else {
								echo "Sorry, there was an error uploading your file.\n";
							$response["success"] = false;
						}
				}*/
}


?>

<?php if($response["success"] == TRUE){
if(!empty($_POST["email"])){
	$requete = "INSERT INTO `patients` (`nom_prenom`, `cin`, `date_naissance`, `telephone`, `sexe`, email, adresse, photo) VALUES('$fullname', '$cin', '$naissance', '$telephone', '$sexe', '$email', '$adresse','$photo')";
}
else{
	$requete = "INSERT INTO `patients` (`nom_prenom`, `cin`, `date_naissance`, `telephone`, `sexe`, email, adresse) VALUES('$fullname', '$cin', '$naissance', '$telephone', '$sexe', null, '$adresse')";
}
$conn->exec($requete);
?>
<script>
	window.location.href= "client.php";
</script>
<?php } ?>