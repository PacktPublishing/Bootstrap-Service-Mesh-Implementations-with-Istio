resource "aws_vpc_peering_connection" "foo" {
  vpc_id      = var.requestor_vpc_id
  peer_vpc_id = var.acceptor_vpc_id
  auto_accept = true
}

resource "aws_vpc_peering_connection_options" "foo" {
  vpc_peering_connection_id = aws_vpc_peering_connection.foo.id

  accepter {
    allow_remote_vpc_dns_resolution = true
  }

  
}